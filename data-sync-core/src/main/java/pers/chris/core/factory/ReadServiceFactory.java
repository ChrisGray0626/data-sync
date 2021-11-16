package pers.chris.core.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.chris.core.service.ReadService;

import java.util.Map;

@Service
public class ReadServiceFactory {

    @Autowired
    private Map<String, ReadService> readServiceMap;

    public ReadService get(String type) {
        return readServiceMap.get(type);
    }

}
