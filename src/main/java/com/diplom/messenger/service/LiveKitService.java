package com.diplom.messenger.service;

import com.diplom.messenger.config.LiveKitConfig;
import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiveKitService {

    private final LiveKitConfig liveKitConfig;

    public String generateToken(String username, String roomName) throws Exception {
        AccessToken token = new AccessToken(liveKitConfig.apiKey, liveKitConfig.apiSecret);
        token.setName(username);
        token.setIdentity(username);
        token.addGrants(new RoomJoin(true), new RoomName(roomName));
        return token.toJwt();
    }

    public String getRoomName(Long callId) {
        return "call-" + callId;
    }
}