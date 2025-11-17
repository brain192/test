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

    // 전체 게시글 개수 구하기
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM board2";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 특정 페이지의 게시글 목록 조회 (LIMIT + OFFSET)
    public List<BoardDTO> findPage(int offset, int pageSize) {
        String sql = "SELECT id, title, writer, created_at FROM board2 ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BoardDTO dto = new BoardDTO();
            dto.setId(rs.getLong("id"));
            dto.setTitle(rs.getString("title"));
            dto.setWriter(rs.getString("writer"));
            dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return dto;
        }, pageSize, offset);
    }

    public void save(BoardDTO board) {
        String sql = "INSERT INTO board2 (title, content, writer, filename, original_filename) VALUES (?, ?, ?, ?, ?)";
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