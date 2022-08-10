ui = true

storage "postgresql" {
  connection_url = "postgres://vault:dev@witoldjnc.ru:5432/vault?sslmode=disable"
}

listener "tcp" {
  address = "witoldjnc.ru:8200"
  tls_disable = true
}

api_addr = "http://witoldjnc.ru:8200"