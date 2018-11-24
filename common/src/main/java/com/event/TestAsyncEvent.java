package com.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.infinispan.remoting.transport.Address;


@Setter
@Getter
@AllArgsConstructor
public class TestAsyncEvent extends EventData {
    private Address address;
}
