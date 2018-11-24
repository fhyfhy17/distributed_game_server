package com.event.event;

import lombok.Data;
import org.infinispan.remoting.transport.Address;

@Data
public class TestAsyncEvent {
    private Address address;
}
