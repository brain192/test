package kjhtest.test.repository;

import kjhtest.test.domain.BoardDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardRepository {

    private final JdbcTemplate jdbcTemplate;

    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(BoardDTO board) {
        String sql = "INSERT INTO board (title, content, writer, filename, original_filename) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, board.getTitle(), board.getContent(), board.getWriter(),
                board.getFilename(), board.getOriginalFilename());
    }

    public List<BoardDTO> findAll() {
        String sql = "SELECT * FROM board2 ORDER BY id DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BoardDTO board = new BoardDTO();
            board.setId(rs.getLong("id"));
            board.setTitle(rs.getString("title"));
            board.setContent(rs.getString("content"));
            board.setWriter(rs.getString("writer"));
            board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return board;
        });
    }

    public BoardDTO findById(Long id) {
        String sql = "SELECT * FROM board2 WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BoardDTO board = new BoardDTO();
            board.setId(rs.getLong("id"));
            board.setTitle(rs.getString("title"));
            board.setContent(rs.getString("content"));
            board.setWriter(rs.getString("writer"));
            board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            board.setFilename(rs.getString("filename"));
            board.setOriginalFilename(rs.getString("original_filename"));
            return board;
        }, id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM board2 WHERE id = ?", id);
    }

    public void update(BoardDTO board) {
        jdbcTemplate.update("UPDATE board2 SET title=?, content=? WHERE id=?",
                board.getTitle(), board.getContent(), board.getId());
    }
}