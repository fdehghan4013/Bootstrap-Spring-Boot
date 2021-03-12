package com.example.demo.service;

import com.example.demo.domain.Agent;
import com.example.demo.domain.dto.AgentDto;

import java.util.List;

public interface AgentService {
    List<AgentDto> getAllDecryptedAgentsWithKeys();
    List<AgentDto> getAllEncryptedAgents();
    void generateNewKeyForAgent(Long agentId);
    void generateNewAgent(Agent agent);
}
