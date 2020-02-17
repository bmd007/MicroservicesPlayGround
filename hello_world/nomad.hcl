job "hello_world" {
  region =      "global"
  datacenters = ["dc1"]
  #  type =        "service"
  type =        "batch"

  #  update {
  # The update stanza specifies the group's update strategy.
  #    max_parallel =     1
  #    health_check =     "checks"
  #    min_healthy_time = "30s"
  #  }


  parameterized {
    meta_required = ["DOCKER_HUB_PASSOWRD"]
  }

 group "hello_world" {
    #count = INSTANCE_COUNT
    count = 1

    restart {
      delay = "15s"
      mode =  "delay"
    }

    task "hello_world" {
      driver = "docker"

      # Configuration is specific to each driver.
      config {
        network_mode = "host"
//        hostname = "hello_world"
        image =      "bmd007/hello_world:latest"
        force_pull = true
        auth {
          username = "bmd007"
          password = "${NOMAD_META_DOCKER_HUB_PASSOWRD}"
        }

        port_map {
          http =  7451
        }
      }

      env {
        SPRING_PROFILES_ACTIVE =                                  "nomad"
        CONFIG_SERVER_IP =                                        "10.71.216.152"
        CONFIG_SERVER_PORT =                                      "8888"
        SERVICE_REGISTRY_SERVER_IP =                              "10.71.216.152"
        SERVICE_REGISTRY_SERVER_PORT =                            "8761"
        SPRING_APPLICATION_INSTANCE_ID =                          "${NOMAD_ALLOC_ID}"
        JAVA_OPTS =                                               "-XshowSettings:vm -XX:+ExitOnOutOfMemoryError -Xmx200m -Xms150m -XX:MaxDirectMemorySize=48m -XX:ReservedCodeCacheSize=64m -XX:MaxMetaspaceSize=128m -Xss256k"
      }
      resources {
        cpu =    256
        memory = 250
        network {
          mode = "host"
          mbits = 1
          port "http" {}
        }
      }
    }
  }
}
