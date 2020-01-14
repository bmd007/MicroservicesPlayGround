job "system-service-registry" {
  region =      "global"
  datacenters = ["dc1"]
  type =        "service"

  update {
    # The update stanza specifies the group's update strategy.
    max_parallel =     1
    health_check =     "checks"
    min_healthy_time = "30s"
  }

 group "system-service-registry" {
    #count = INSTANCE_COUNT
    count = 1

    restart {
      delay = "15s"
      mode =  "delay"
    }

    task "system-service-registry" {
      driver = "docker"

      # Configuration is specific to each driver.
      config {
        image =      "bmd007/system-service-registry:latest"
        force_pull = true
        auth {
          username = "bmd007"
          password = ""
        }

        port_map {
          http =       8761
        }
      }

      env {
        SPRING_PROFILES_ACTIVE =                                  "nomad"
        EUREKA_INSTANCE_HOSTNAME =                                "${NOMAD_IP_http}"
        INSTANCE_NOMAD_PORT =                                     "${NOMAD_HOST_PORT_http}"
        CONFIG_SERVER_IP =                                        "http://172.17.0.3"
        CONFIG_SERVER_PORT =                                      "21379"
        SPRING_APPLICATION_INSTANCE_ID =                          "${NOMAD_ALLOC_ID}"
        JAVA_OPTS =                                               "-XshowSettings:vm -XX:+ExitOnOutOfMemoryError -Xmx200m -Xms150m -XX:MaxDirectMemorySize=48m -XX:ReservedCodeCacheSize=64m -XX:MaxMetaspaceSize=128m -Xss256k"
      }
      resources {
        cpu =    256
        memory = 250
        network {
          mbits = 1
          port "http" {}
        }
      }
    }
  }
}
