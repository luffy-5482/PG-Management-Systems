package com.parent.tenant.controller;

import org.springframework.web.bind.annotation.*;
import com.parent.tenant.dto.*;
import com.parent.tenant.enums.ApplicationStep;
import com.parent.tenant.model.TenantApplication;
import com.parent.tenant.service.TenantRegistrationService;

@RestController
@RequestMapping("/api/tenant/register")
public class TenantRegistrationController {

    private final TenantRegistrationService service;

    public TenantRegistrationController(TenantRegistrationService service) {
        this.service = service;
    }

    // STEP 1: VERIFY PG - already perfect
    @PostMapping("/verifyPg")
    public VerifyPgResponse verifyPg(@RequestBody VerifyPgRequest req) {
        TenantApplication app = service.verifyPg(req.pgId);
        VerifyPgResponse res = new VerifyPgResponse();
        res.applicationId = app.getId();
        res.pgId = req.pgId;
        res.nextStep = app.getCurrentStep().name();
        return res;
    }

    // STEP 2: PERSONAL DETAILS - now returns nextStep
    @PostMapping("/{applicationId}/personal-details")
    public StepResponse submitPersonalDetails(
            @PathVariable Long applicationId,
            @RequestBody TenantPersonalDetailsRequest req
    ) {
        service.submitPersonalDetails(applicationId, req);
        return new StepResponse(ApplicationStep.DOCUMENTS.name());
    }

    // STEP 3: DOCUMENTS - now returns nextStep
    @PostMapping("/{applicationId}/documents")
    public StepResponse submitDocuments(
            @PathVariable Long applicationId,
            @RequestBody TenantDocumentsRequest req
    ) {
        service.submitDocuments(applicationId, req);
        return new StepResponse(ApplicationStep.REVIEW.name());
    }

    // STEP 4: SUBMIT FOR REVIEW - final step
    @PostMapping("/{applicationId}/submit")
    public StepResponse submitForReview(@PathVariable Long applicationId) {
        service.submitForReview(applicationId);
        return new StepResponse("COMPLETED");
    }
}

// Simple, clean response class - add this inside the same file (or in dto package)
class StepResponse {
    public String nextStep;

    public StepResponse(String nextStep) {
        this.nextStep = nextStep;
    }
}