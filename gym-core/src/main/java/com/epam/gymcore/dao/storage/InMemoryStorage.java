package com.epam.gymcore.dao.storage;

import com.epam.gymcore.domain.model.Identifiable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryStorage<T extends Identifiable<Long>> implements InitializingBean {
    @Autowired
    private ObjectMapper mapper;
    private final Map<Long,T> storage = new HashMap<>();

    private String resourcePath;
    private Class<? extends T> dataType;

    private Long index = 0L;

    public InMemoryStorage(String resourcePath,Class<? extends T> dataType) {
        this.resourcePath = resourcePath;
        this.dataType = dataType;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (resourcePath != null && !resourcePath.isBlank()) {
            List<T> dataList = mapper.readValue(new File(resourcePath),
                    mapper.getTypeFactory().constructCollectionType(List.class, dataType));
            for (T item : dataList) {
                storage.put(item.getId(), item);
            }
        }
        index = (long) storage.size();
    }

    public Map<Long,T> getStorage(){
        return storage;
    }

    public Long getNextIndex(){
        return index++;
    }
}
