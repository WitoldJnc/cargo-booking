ui = true

storage "postgresql" {
  connection_url = "postgres://<username>:<password>@<host>:<port>/<db>?sslmode=disable"
}

listener "tcp" {
  address = "<host>:8200"
  tls_disable = true
}

api_addr = "http://<host>:8200"