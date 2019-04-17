package ir.tiroon.microservices.event

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import java.time.LocalDateTime


class Event {

    UUID id
    //TODO check what type of time is suitable here
    LocalDateTime time

    @JsonCreator
    Event(@JsonProperty("id") UUID id, @JsonProperty("time") LocalDateTime time) {
        this.id = id
        this.time = time
    }
}
