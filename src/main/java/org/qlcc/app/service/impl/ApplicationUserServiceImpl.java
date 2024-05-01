package org.qlcc.app.service.impl;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.ApplicationUser;
import org.qlcc.app.repository.ApplicationUserRepository;
import org.qlcc.app.repository.UserRepository;
import org.qlcc.app.service.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserServiceImpl.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final UserRepository userRepository;

    public ApplicationUserServiceImpl(ApplicationUserRepository applicationUserRepository, UserRepository userRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApplicationUser save(ApplicationUser applicationUser) {
        log.debug("Request to save ApplicationUser : {}", applicationUser);
        Long userId = applicationUser.getInternalUser().getId();
        userRepository.findById(userId).ifPresent(applicationUser::internalUser);
        return applicationUserRepository.save(applicationUser);
    }

    @Override
    public ApplicationUser update(ApplicationUser applicationUser) {
        log.debug("Request to update ApplicationUser : {}", applicationUser);
        return applicationUserRepository.save(applicationUser);
    }

    @Override
    public Optional<ApplicationUser> partialUpdate(ApplicationUser applicationUser) {
        log.debug("Request to partially update ApplicationUser : {}", applicationUser);

        return applicationUserRepository
            .findById(applicationUser.getId())
            .map(existingApplicationUser -> {
                if (applicationUser.getCreateAt() != null) {
                    existingApplicationUser.setCreateAt(applicationUser.getCreateAt());
                }
                if (applicationUser.getUpdateAt() != null) {
                    existingApplicationUser.setUpdateAt(applicationUser.getUpdateAt());
                }
                if (applicationUser.getDeletedAt() != null) {
                    existingApplicationUser.setDeletedAt(applicationUser.getDeletedAt());
                }
                if (applicationUser.getFamilyId() != null) {
                    existingApplicationUser.setFamilyId(applicationUser.getFamilyId());
                }
                if (applicationUser.getCitizenID() != null) {
                    existingApplicationUser.setCitizenID(applicationUser.getCitizenID());
                }
                if (applicationUser.getName() != null) {
                    existingApplicationUser.setName(applicationUser.getName());
                }
                if (applicationUser.getDob() != null) {
                    existingApplicationUser.setDob(applicationUser.getDob());
                }
                if (applicationUser.getContact() != null) {
                    existingApplicationUser.setContact(applicationUser.getContact());
                }
                if (applicationUser.getGender() != null) {
                    existingApplicationUser.setGender(applicationUser.getGender());
                }
                if (applicationUser.getRelation() != null) {
                    existingApplicationUser.setRelation(applicationUser.getRelation());
                }
                if (applicationUser.getStatus() != null) {
                    existingApplicationUser.setStatus(applicationUser.getStatus());
                }
                if (applicationUser.getRoomId() != null) {
                    existingApplicationUser.setRoomId(applicationUser.getRoomId());
                }

                return existingApplicationUser;
            })
            .map(applicationUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationUser> findAll() {
        log.debug("Request to get all ApplicationUsers");
        return applicationUserRepository.findAll();
    }

    public Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable) {
        return applicationUserRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationUser> findOne(Long id) {
        log.debug("Request to get ApplicationUser : {}", id);
        return applicationUserRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
    }
}
