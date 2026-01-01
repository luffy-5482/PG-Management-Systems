package com.parent.tenant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parent.pg.repository.PgRepository;
import com.parent.tenant.dto.TenantDocumentsRequest;
import com.parent.tenant.dto.TenantPersonalDetailsRequest;
import com.parent.tenant.enums.ApplicationStep;
import com.parent.tenant.enums.TenantStatus;
import com.parent.tenant.model.TenantApplication;
import com.parent.tenant.model.TenantDocument;
import com.parent.tenant.model.TenantProfile;
import com.parent.tenant.repository.TenantApplicationRepository;
import com.parent.tenant.repository.TenantDocumentRepository;
import com.parent.tenant.repository.TenantProfileRepository;

@Service
public class TenantRegistrationService {

    private final TenantApplicationRepository appRepo;
    private final TenantProfileRepository profileRepo;
    private final TenantDocumentRepository docRepo;
    private final PgRepository pgRepository;
    private final TenantActivityLogService logService;

    public TenantRegistrationService(
            TenantApplicationRepository appRepo,
            TenantProfileRepository profileRepo,
            TenantDocumentRepository docRepo,
            PgRepository pgRepository,
            TenantActivityLogService logService
    ) {
        this.appRepo = appRepo;
        this.profileRepo = profileRepo;
        this.docRepo = docRepo;
        this.pgRepository = pgRepository;
        this.logService = logService;
    }

    // --------------------------------------------------
    // STEP 1: VERIFY PG & CREATE APPLICATION
    // --------------------------------------------------
    @Transactional
    public TenantApplication verifyPg(String pgId) {

        if (!checkPgExists(pgId))
            throw new IllegalArgumentException("Invalid PG");

        TenantApplication app = new TenantApplication();
        app.setPgId(pgId);
        app.setStatus(TenantStatus.DRAFT);
        app.setCurrentStep(ApplicationStep.PERSONAL_DETAILS);

        // Note: email and phone are left as null here (allowed now because @Column(nullable = true))

        app = appRepo.save(app);

        logService.log(
                app.getId(),
                "PG_VERIFIED",
                "PG verified, draft application created"
        );

        return app;
    }

    // --------------------------------------------------
    // STEP 2: PERSONAL DETAILS
    // --------------------------------------------------
    @Transactional
    public void submitPersonalDetails(
            Long appId,
            TenantPersonalDetailsRequest req
    ) {

        TenantApplication app = getApp(appId, ApplicationStep.PERSONAL_DETAILS);

        TenantProfile profile = profileRepo
                .findByApplicationId(appId)
                .orElse(new TenantProfile());

        profile.setApplicationId(appId);
        profile.setFirstName(req.getFirstName());
        profile.setLastName(req.getLastName());
        profile.setDob(req.getDob());
        profile.setGender(req.getGender());
        profile.setOccupation(req.getOccupation());
        profile.setAddress(req.getAddress());
        profile.setCity(req.getCity());
        profile.setPincode(req.getPincode());
        profile.setEmergencyContact(
                req.getEmergencyContactName() + " | " +
                req.getEmergencyContactPhone() + " | " +
                req.getEmergencyContactRelation()
        );
        profile.setPhotoUrl(req.getPhotoUrl());

        profileRepo.save(profile);

        // ============ CRITICAL FIX: Set email & phone on the application ============
        app.setEmail(req.getEmail());
        app.setPhone(req.getPhone());
        // ===========================================================================

        app.setStatus(TenantStatus.PERSONAL_DETAILS_SUBMITTED);
        app.setCurrentStep(ApplicationStep.DOCUMENTS);
        appRepo.save(app);

        logService.log(
                appId,
                "PERSONAL_DETAILS_SUBMITTED",
                "Tenant submitted personal details"
        );
    }

    // --------------------------------------------------
    // STEP 3: DOCUMENT UPLOAD
    // --------------------------------------------------
    @Transactional
    public void submitDocuments(
            Long appId,
            TenantDocumentsRequest req
    ) {

        TenantApplication app = getApp(appId, ApplicationStep.DOCUMENTS);

        TenantDocument docs = docRepo
                .findByApplicationId(appId)
                .orElse(new TenantDocument());

        docs.setApplicationId(appId);

        docs.setAadhaarNumber(req.getAadhaarNumber());
        docs.setAadhaarFrontUrl(req.getAadhaarFrontUrl());
        docs.setAadhaarBackUrl(req.getAadhaarBackUrl());

        docs.setPanNumber(req.getPanNumber());
        docs.setPanUrl(req.getPanCardUrl());

        docs.setPhotoUrl(req.getPhotoUrl());
        docs.setEducationCertificateUrl(req.getEducationCertificateUrl());

        docRepo.save(docs);

        app.setStatus(TenantStatus.DOCUMENTS_SUBMITTED);
        app.setCurrentStep(ApplicationStep.REVIEW);
        appRepo.save(app);

        logService.log(
                appId,
                "DOCUMENTS_SUBMITTED",
                "Tenant uploaded documents"
        );
    }

    // --------------------------------------------------
    // STEP 4: SUBMIT FOR REVIEW
    // --------------------------------------------------
    @Transactional
    public void submitForReview(Long appId) {

        TenantApplication app = getApp(appId, ApplicationStep.REVIEW);

        // Optional safety check: ensure email and phone are provided before final submission
        if (app.getEmail() == null || app.getEmail().trim().isEmpty() ||
            app.getPhone() == null || app.getPhone().trim().isEmpty()) {
            throw new IllegalStateException("Email and phone number are required before submitting the application for review");
        }

        app.setStatus(TenantStatus.UNDER_REVIEW);
        appRepo.save(app);

        logService.log(
                appId,
                "UNDER_REVIEW",
                "Tenant submitted application for review"
        );
    }

    // --------------------------------------------------
    // INTERNAL HELPERS
    // --------------------------------------------------
    private TenantApplication getApp(Long id, ApplicationStep step) {

        TenantApplication app = appRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application"));

        if (app.getCurrentStep() != step)
            throw new IllegalStateException("Invalid workflow step");

        return app;
    }

    private boolean checkPgExists(String pgId) {

        if (pgId == null)
            return false;

        try {
            Long id = Long.valueOf(pgId);
            return pgRepository.existsById(id);
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}