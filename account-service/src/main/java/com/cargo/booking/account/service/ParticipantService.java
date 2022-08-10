package com.cargo.booking.account.service;

import com.cargo.booking.account.dto.SettingsDto;
import com.cargo.booking.account.dto.UserWorkspaceRoleDto;
import com.cargo.booking.account.dto.UserWorkspaceRoleInfoDto;
import com.cargo.booking.account.dto.company.NewCompanyDto;
import com.cargo.booking.account.dto.participant.*;
import com.cargo.booking.account.dto.user.UserInfoDto;
import com.cargo.booking.account.mapper.CompanyMapper;
import com.cargo.booking.account.mapper.ParticipantMapper;
import com.cargo.booking.account.mapper.UserMapper;
import com.cargo.booking.account.model.*;
import com.cargo.booking.account.repository.ParticipantDao;
import com.cargo.booking.account.repository.jpa.*;
import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private static final String PARTICIPANT_NON_EXISTENT = "participant.non_existent";

    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final ParticipantMapper participantMapper;

    private final ParticipantDao participantDao;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ParticipantRepository participantRepository;
    private final WorkspaceRoleRepository workspaceRoleRepository;
    private final ParticipantUserRepository participantUserRepository;

    @Transactional
    public ServiceMessage createParticipant(NewParticipantDto participantDto) {
        Settings settings = new Settings();
        settings.setValue(JsonSettings.builder()
                .cargoAgentAirlines(participantDto.getSettings().getCargoAgentAirlines().toArray(new UUID[0]))
                .build());

        Participant newParticipant = new Participant();

        if (participantDto.getParticipantType().equals(ParticipantType.COMPANY)) {
            NewCompanyDto companyDto = participantDto.getCompany();
            Company newCompany = companyMapper.newCompanyDtoToCompany(companyDto);
            Company savedCompany = companyRepository.save(newCompany);
            newParticipant.setCompany(savedCompany);
        }
        newParticipant.setStatus(ParticipantStatus.ACTIVE);
        newParticipant.setType(ParticipantType.COMPANY);
        newParticipant.setSettings(settings);
        Participant savedParticipant = participantRepository.save(newParticipant);

        Map<UUID, List<UserWorkspaceRoleDto>> workspacesByUsers = participantDto.getWorkspaces().stream()
                .collect(Collectors.groupingBy(UserWorkspaceRoleDto::getUserId));

        for (Map.Entry<UUID, List<UserWorkspaceRoleDto>> entry : workspacesByUsers.entrySet()) {
            Optional<User> maybeUser = userRepository.findById(entry.getKey());
            if (maybeUser.isEmpty()) {
                throw new ServiceException(new ServiceMessage("user.non_existent"));
            }
            User user = maybeUser.get();
            ParticipantUser participantUser = new ParticipantUser();
            participantUser.setParticipant(savedParticipant);
            participantUser.setAdministrator(false);
            processWorkspaceRoles(entry.getValue(), participantUser);
            participantUser.setUser(user);
            ParticipantUser savedParticipantUser = participantUserRepository.save(participantUser);
            user.getParticipantUsers().add(savedParticipantUser);
        }

        return new ServiceMessage("participant.created");
    }

    @Transactional
    public ServiceMessage deleteParticipant(UUID participantId) {
        Optional<Participant> maybeParticipant = participantRepository.findById(participantId);
        if (maybeParticipant.isEmpty()) {
            throw new ServiceException(new ServiceMessage(PARTICIPANT_NON_EXISTENT));
        }
        maybeParticipant.get().setStatus(ParticipantStatus.DELETED);
        return new ServiceMessage("participant.deleted");
    }

    @Transactional
    public ServiceMessage restoreParticipant(UUID participantId) {
        Optional<Participant> maybeParticipant = participantRepository.findById(participantId);
        if (maybeParticipant.isEmpty()) {
            throw new ServiceException(new ServiceMessage(PARTICIPANT_NON_EXISTENT));
        }
        maybeParticipant.get().setStatus(ParticipantStatus.ACTIVE);
        return new ServiceMessage("participant.restored");
    }

    @Transactional(readOnly = true)
    public Page<DetailedParticipantDto> searchParticipants(Pageable pageable, ParticipantStatus participantStatus,
                                                           ParticipantType participantType, String query) {
        Page<Participant> participants = participantDao.search(query, participantStatus, participantType, pageable);
        return participants.map(p -> {
            DetailedParticipantDto dto = participantMapper.participantToDetailedParticipantDto(p);
            dto.setCompany(companyMapper.companyToShortCompanyDto(p.getCompany()));
            dto.setWorkspacesNames(p.getParticipantUsers().stream()
                    .flatMap(participantUser -> participantUser.getWorkspaceRoles().stream())
                    .map(wr -> wr.getWorkspace().getName()).collect(Collectors.toSet()));
            return dto;
        });
    }

    @Transactional(readOnly = true)
    public ParticipantInfoDto getParticipantInfo(UUID participantId) {
        Optional<Participant> maybeParticipant = participantRepository.findById(participantId);
        if (maybeParticipant.isEmpty()) {
            throw new ServiceException(new ServiceMessage(PARTICIPANT_NON_EXISTENT));
        }
        Participant participant = maybeParticipant.get();

        ParticipantInfoDto participantInfoDto = new ParticipantInfoDto();
        participantInfoDto.setType(participant.getType());
        participantInfoDto.setStatus(participant.getStatus());
        if (participant.getType().equals(ParticipantType.COMPANY)) {
            participantInfoDto.setCompany(companyMapper.companyToCompanyDto(participant.getCompany()));
        }
        participant.getParticipantUsers().forEach(pu -> {
            UserInfoDto userDto = userMapper.userToUserInfoDto(pu.getUser());
            pu.getWorkspaceRoles().forEach(wr -> participantInfoDto.getWorkspaces().add(
                    new UserWorkspaceRoleInfoDto(userDto, wr.getId())));
        });
        SettingsDto settingsDto = new SettingsDto();
        settingsDto.getCargoAgentAirlines().addAll(List.of(participant.getSettings().getValue().getCargoAgentAirlines()));
        participantInfoDto.setSettings(settingsDto);
        return participantInfoDto;
    }

    @Transactional
    public ServiceMessage updateParticipantInfo(UUID participantId, UpdateParticipantInfoDto dto) {
        Optional<Participant> maybeParticipant = participantRepository.findById(participantId);
        if (maybeParticipant.isEmpty()) {
            throw new ServiceException(new ServiceMessage(PARTICIPANT_NON_EXISTENT));
        }
        if (dto.getParticipantType().equals(ParticipantType.COMPANY)) {
            Company company = companyMapper.newCompanyDtoToCompany(dto.getCompany());
            updateCompanyFields(maybeParticipant.get().getCompany(), company);
        }
        return new ServiceMessage("participant.info_updated");
    }

    private void updateCompanyFields(Company target, Company source) {
        target.setShortName(source.getShortName());
        target.setFullName(source.getFullName());
        target.setInn(source.getInn());
        target.setKpp(source.getKpp());
        target.setPostAddress(source.getPostAddress());
        target.setLegalAddress(source.getLegalAddress());
        target.setDescription(source.getDescription());
        target.setContactPerson(source.getContactPerson());
        target.setPosition(source.getPosition());
        target.setPhone(source.getPhone());
        target.setEmail(source.getEmail());
    }

    @Transactional
    public ServiceMessage updateParticipantWorkspaces(UUID participantId, UpdateParticipantWorkspacesDto dto) {
        Optional<Participant> maybeParticipant = participantRepository.findById(participantId);
        if (maybeParticipant.isEmpty()) {
            throw new ServiceException(new ServiceMessage(PARTICIPANT_NON_EXISTENT));
        }

        Set<ParticipantUser> allParticipantUsers = maybeParticipant.get().getParticipantUsers();
        Map<UUID, List<UserWorkspaceRoleDto>> workspacesByUsers = dto.getWorkspaces().stream()
                .collect(Collectors.groupingBy(UserWorkspaceRoleDto::getUserId));
        for (Map.Entry<UUID, List<UserWorkspaceRoleDto>> entry : workspacesByUsers.entrySet()) {
            Optional<User> maybeUser = userRepository.findById(entry.getKey());
            if (maybeUser.isEmpty()) {
                throw new ServiceException(new ServiceMessage("user.non_existent"));
            }
            User user = maybeUser.get();
            boolean isParticipantMember = false;
            for (ParticipantUser participantUser : user.getParticipantUsers()) {
                if (participantUser.getParticipant().getId().equals(participantId)) {
                    isParticipantMember = true;
                    participantUser.setWorkspaceRoles(new LinkedHashSet<>());
                    processWorkspaceRoles(entry.getValue(), participantUser);
                    allParticipantUsers.remove(participantUser);
                }
            }
            if (!isParticipantMember) {
                ParticipantUser participantUser = new ParticipantUser();
                participantUser.setParticipant(maybeParticipant.get());
                participantUser.setAdministrator(false);
                processWorkspaceRoles(entry.getValue(), participantUser);
                participantUser.setUser(user);
                user.getParticipantUsers().add(participantUserRepository.save(participantUser));
            }
        }

        // Если для пользователя участника не был указан ни один личный кабинет, то все имеющиеся связи с ЛК удаляются
        for (ParticipantUser participantUser : allParticipantUsers) {
            participantUser.setWorkspaceRoles(new LinkedHashSet<>());
        }
        return new ServiceMessage("participant.workspaces_updated");
    }

    private void processWorkspaceRoles(List<UserWorkspaceRoleDto> workspaceRoles, ParticipantUser participantUser) {
        for (UserWorkspaceRoleDto workspaceRoleDto : workspaceRoles) {
            Optional<WorkspaceRole> maybeWorkspaceRole = workspaceRoleRepository.findById(workspaceRoleDto.getWorkspaceRoleId());
            if (maybeWorkspaceRole.isEmpty()) {
                throw new ServiceException(new ServiceMessage("workspace_role.non_existent"));
            }
            participantUser.getWorkspaceRoles().add(maybeWorkspaceRole.get());
        }
    }

    @Transactional
    public ServiceMessage updateParticipantSettings(UUID participantId, UpdateParticipantSettingsDto dto) {
        Optional<Participant> maybeParticipant = participantRepository.findById(participantId);
        if (maybeParticipant.isEmpty()) {
            throw new ServiceException(new ServiceMessage(PARTICIPANT_NON_EXISTENT));
        }
        maybeParticipant.get().getSettings().getValue()
                .setCargoAgentAirlines(dto.getSettings().getCargoAgentAirlines().toArray(new UUID[0]));
        return new ServiceMessage("participant.settings_updated");
    }

    public ServiceMessage addCompanyLogo(UUID participantId, MultipartFile logo) {
        Optional<Participant> maybeParticipant = participantRepository.findById(participantId);
        if (maybeParticipant.isEmpty()) {
            throw new ServiceException(new ServiceMessage(PARTICIPANT_NON_EXISTENT));
        }

        if (maybeParticipant.get().getType().equals(ParticipantType.COMPANY)) {
            Company company = maybeParticipant.get().getCompany();
            companyRepository.save(company);
            return new ServiceMessage("company.add_logo_success");
        } else {
            throw new ServiceException(new ServiceMessage("participant.company_non_existent"));
        }
    }

    public Set<String> getEmailsByParticipantAndWorkspace(UUID participantId, UUID workspaceRoleId) {
        return participantUserRepository.getEmailsByParticipantIdAndWorkspaceRoleId(participantId, workspaceRoleId);
    }

    public String getCompanyEmailByParticipant(UUID participantId) {
        return participantRepository.findById(participantId).get().getCompany().getEmail();
    }
}
