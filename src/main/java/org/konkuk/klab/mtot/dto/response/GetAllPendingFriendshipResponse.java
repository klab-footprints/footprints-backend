package org.konkuk.klab.mtot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllPendingFriendshipResponse {
    private List<PendingFriendshipResponse> pendingFriendships;
}
