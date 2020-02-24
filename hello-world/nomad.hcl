job "hello-world" {
  region =      "global"
  datacenters = ["dc1"]
  type =        "batch"


  parameterized {
    meta_required = ["DOCKER_HUB_PASSOWRD"]
  }

 group "hello-world" {
    #count = INSTANCE_COUNT
    count = 1

    restart {
      delay = "15s"
      mode =  "delay"
    }

    task "hello-world" {
      driver = "docker"

      config {
        network_mode = "host"
//        hostname = "hello-world"
        image =      "bmd007/hello-world:latest"
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
        CONFIG_SERVER_IP =                                        "http://config-center"
        CONFIG_SERVER_PORT =                                      "8888"
        SPRING_CLOUD_CONSUL_HOST =                                "${NOMAD_IP_http}"
        #        SPRING_APPLICATION_INSTANCE_ID =                           "${NOMAD_ALLOC_ID}"
        SPRING_CLOUD_SERVICE_REGISTRY_AUTO_REGISTRATION_ENABLED = "false"
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
