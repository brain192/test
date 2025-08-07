package kjhtest.test.service;

import kjhtest.test.domain.MemberDTO;
import kjhtest.test.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public void register(MemberDTO member) {
        repository.save(member);
    }

    public Optional<MemberDTO> login(String username, String password) {
        return repository.findByUsername(username)
                .filter(member -> member.getPassword().equals(password));
    }
}