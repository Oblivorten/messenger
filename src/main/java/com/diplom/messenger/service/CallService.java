package com.diplom.messenger.service;

import com.diplom.messenger.dto.request.InitiateCallRequest;
import com.diplom.messenger.dto.response.CallHistoryResponse;
import com.diplom.messenger.dto.response.CallResponse;

import java.util.List;

public interface CallService {
    CallResponse initiateCall(String username, InitiateCallRequest request);
    CallResponse acceptCall(String username, Long callId);
    void rejectCall(String username, Long callId);
    void endCall(String username, Long callId);
    List<CallHistoryResponse> getHistory(String username);
}