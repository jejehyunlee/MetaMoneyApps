package agile.metamoney.service.impl;

import agile.metamoney.entity.Role;
import agile.metamoney.entity.constant.ERole;
import agile.metamoney.repository.RoleRepository;
import agile.metamoney.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getOrsave(ERole erole) {
        return roleRepository.findByRole(erole).orElseGet(
                () -> roleRepository.save(Role.builder()
                        .role(erole)
                        .build())
        );
    }
}
