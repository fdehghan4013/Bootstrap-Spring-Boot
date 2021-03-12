package com.example.demo.service.impl;

import com.example.demo.domain.Agent;
import com.example.demo.domain.Key;
import com.example.demo.domain.dto.AgentDto;
import com.example.demo.domain.dto.KeyDto;
import com.example.demo.repository.AgentRepository;
import com.example.demo.repository.KeyRepository;
import com.example.demo.service.AgentService;
import com.example.demo.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final AgentRepository agentRepository;
    private final KeyRepository keyRepository;
    private final EncryptionUtil encryptionUtil;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository, KeyRepository keyRepository, EncryptionUtil encryptionUtil) {
        this.agentRepository = agentRepository;
        this.keyRepository = keyRepository;
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    public List<AgentDto> getAllDecryptedAgentsWithKeys() {
        Iterable<Agent> agents = agentRepository.findAll();

        return mapToDecrypt(agents);
    }

    @Override
    public List<AgentDto> getAllEncryptedAgents() {
        Iterable<Agent> agents = agentRepository.findAll();
        return mapperToEncrypt(agents);
    }

    @Override
    public void generateNewKeyForAgent(Long agentId) {
        Agent agent = agentRepository.findById(agentId).orElseThrow(IllegalArgumentException::new);

        Key key = new Key();
        key.setAgent(agent);
        key.setValue(encryptionUtil.encryptAndEncodeBase64(UUID.randomUUID().toString()));

        keyRepository.save(key);
    }

    @Override
    public void generateNewAgent(Agent agent) {
        agent.setFirstName(encryptionUtil.encryptAndEncodeBase64(agent.getFirstName()));
        agent.setLastName(encryptionUtil.encryptAndEncodeBase64(agent.getLastName()));

        agentRepository.save(agent);
    }

    private List<AgentDto> mapperToEncrypt(Iterable<Agent> agents) {
        List<AgentDto> result = new ArrayList<>();

        for (Agent agent : agents) result.add(mapperToEncrypt(agent));

        return result;
    }

    private List<AgentDto> mapToDecrypt(Iterable<Agent> agents) {
        List<AgentDto> result = new ArrayList<>();

        for (Agent agent : agents) result.add(mapToDecrypt(agent));

        return result;
    }

    private AgentDto mapToDecrypt(Agent agentEntity) {
        AgentDto agentDto = new AgentDto();
        agentDto.setId(agentEntity.getId());
        agentDto.setFirstName(encryptionUtil.decodeBase64AndDecrypt(agentEntity.getFirstName()));
        agentDto.setLastName(encryptionUtil.decodeBase64AndDecrypt(agentEntity.getLastName()));
        agentDto.setCreatedDate(sdf.format(agentEntity.getCreatedDate()));

        agentDto.setKeys(mapKeyEntityToDecryptedDto(agentEntity.getKeys()));

        return agentDto;
    }

    private List<KeyDto> mapKeyEntityToDecryptedDto(List<Key> keys) {
        return keys.stream().map(this::mapKeyEntityToDecryptedDto).collect(Collectors.toList());
    }

    private KeyDto mapKeyEntityToDecryptedDto(Key key) {
        KeyDto keyDto = new KeyDto();
        keyDto.setId(key.getId());
        keyDto.setValue(encryptionUtil.decodeBase64AndDecrypt(key.getValue()));

        return keyDto;
    }

    private AgentDto mapperToEncrypt(Agent agentEntity) {
        AgentDto agentDto = new AgentDto();
        agentDto.setId(agentEntity.getId());
        agentDto.setFirstName(encryptionUtil.encryptAndEncodeBase64(agentEntity.getFirstName()));
        agentDto.setLastName(encryptionUtil.encryptAndEncodeBase64(agentEntity.getLastName()));
        agentDto.setCreatedDate(sdf.format(agentEntity.getCreatedDate()));
        return agentDto;

    }

}
