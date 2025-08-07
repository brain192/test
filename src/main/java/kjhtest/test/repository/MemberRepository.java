package kjhtest.test.repository;

import kjhtest.test.domain.MemberDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(MemberDTO member) {
        String sql = "INSERT INTO member2 (username, password, name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, member.getUsername(), member.getPassword(), member.getName());
    }

    public Optional<MemberDTO> findByUsername(String username) {
        String sql = "SELECT * FROM member2 WHERE username = ?";
        return jdbcTemplate.query(sql, (ResultSet rs) -> {
            if (rs.next()) {
                MemberDTO m = new MemberDTO();
                m.setId(rs.getLong("id"));
                m.setUsername(rs.getString("username"));
                m.setPassword(rs.getString("password"));
                m.setName(rs.getString("name"));
                return Optional.of(m);
            }
            return Optional.empty();
        }, username);
    }
}