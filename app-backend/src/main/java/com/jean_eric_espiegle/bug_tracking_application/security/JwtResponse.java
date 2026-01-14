package com.jean_eric_espiegle.bug_tracking_application.security;

import com.jean_eric_espiegle.bug_tracking_application.dto.AccountPlanDto;
import com.jean_eric_espiegle.bug_tracking_application.dto.MembershipDto;
import java.util.List;

public record JwtResponse(String token, String refreshToken, String membershipStatus, AccountPlanDto accountPlan, List<MembershipDto> memberships) {
}
