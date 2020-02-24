job "message-search" {
  region =      "global"
  datacenters = ["dc1"]
  type =        "service"

  update {
    # The update stanza specifies the group's update strategy.
    max_parallel =     1
    health_check =     "checks"
    min_healthy_time = "30s"
  }

 group "message-search" {
    #count = INSTANCE_COUNT
    count = 2

    restart {
      delay = "15s"
      mode =  "delay"
    }

    task "message-search" {
      driver = "docker"

      # Configuration is specific to each driver.
      config {
        image =      "bmd007/message-search:latest"
        force_pull = true
        auth {
          username = "bmd007"
          password = ""
        }

        port_map {
          http =  8081
        }
      }

      env {
        SPRING_PROFILES_ACTIVE =                                  "nomad"
        CONFIG_SERVER_IP =                                        "http://config-center"
        CONFIG_SERVER_PORT =                                      "8888"
        SPRING_CLOUD_CONSUL_HOST =                                "${NOMAD_IP_http}"
        #        SPRING_APPLICATION_INSTANCE_ID =                           "${NOMAD_ALLOC_ID}"
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
