package ru.project.bots.model.configs;

import ru.project.bots.dto.SubstanceDTO;
import ru.project.bots.dto.SubstanceType;
import ru.project.bots.model.logs.SimpleLogger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SubstancesConfigFile implements SubstancesConfig {

    private final SimpleLogger logger;
    private final String substancesFilePath;

    public SubstancesConfigFile(SimpleLogger logger, String substancesFilePath) {
        this.logger = logger;
        this.substancesFilePath = substancesFilePath;
    }

    @Override
    public Map<Long, SubstanceDTO> uploadSubstances() {
        try {

            Path path = Paths.get(substancesFilePath);
            if(!Files.exists(path))
                return Collections.emptyMap();

            final Map<Long, SubstanceDTO> map = new HashMap<>();
            final AtomicInteger counter = new AtomicInteger(1);

            Files.lines(path).forEach(line -> {
                final String[] sub = line.split(",");
                map.put((long) counter.get(), new SubstanceDTO(counter.getAndIncrement(), sub[0], Integer.parseInt(sub[1]), getType(sub[2])));
            });

            return map;

        }catch (Exception e){
            logger.log(e);
            return Collections.emptyMap();
        }
    }

    private SubstanceType getType(String s){
        if("1".equals(s))
            return SubstanceType.DUST;
        else if("2".equals(s))
            return SubstanceType.TABLET;
        else
            return SubstanceType.PACK;
    }
}
