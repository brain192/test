package kjhtest.test.repository;

import kjhtest.test.domain.BoardDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * 게시판 Repository
 * - board 테이블 DB 접근 담당
 * - JdbcTemplate 기반 CRUD 처리
 */
@Repository
public class BoardRepository {

    // JDBC 접근을 도와주는 Spring 클래스
    private final JdbcTemplate jdbc;

    // 생성자 주입
    public BoardRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * 게시글 저장
     * @param board 저장할 게시글 정보
     * @return 생성된 게시글 ID(PK)
     */
    public long save(BoardDTO board) {

        String sql =
                "INSERT INTO board2 " +
                        "(title, content, writer, filename, original_filename) " +
                        "VALUES (?, ?, ?, ?, ?)";

        // 자동 생성 PK 반환용
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {

            // PK 반환 설정
            PreparedStatement ps =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, board.getTitle());
            ps.setString(2, board.getContent());
            ps.setString(3, board.getWriter());
            ps.setString(4, board.getFilename());
            ps.setString(5, board.getOriginalFilename());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return (key != null) ? key.longValue() : 0L;
    }

    /**
     * 페이징된 게시글 목록 조회
     *
     * @param offset 시작 위치
     * @param pageSize 페이지 크기
     */
    public List<BoardDTO> findPage(int offset, int pageSize) {

        String sql =
                "SELECT id, title, writer, created_at " +
                        "FROM board2 " +
                        "ORDER BY id DESC " +
                        "LIMIT ? OFFSET ?";

        return jdbc.query(sql, (rs, rowNum) -> {
            BoardDTO b = new BoardDTO();
            b.setId(rs.getLong("id"));
            b.setTitle(rs.getString("title"));
            b.setWriter(rs.getString("writer"));
            b.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return b;
        }, pageSize, offset);
    }

    /**
     * 전체 게시글 개수 조회
     */
    public int countAll() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM board2", Integer.class);
    }

    /**
     * 전체 게시글 조회 (관리자 / 테스트용)
     */
    public List<BoardDTO> findAll() {

        String sql = "SELECT * FROM board2 ORDER BY id DESC";

        return jdbc.query(sql, (rs, rn) -> {
            BoardDTO b = new BoardDTO();
            b.setId(rs.getLong("id"));
            b.setTitle(rs.getString("title"));
            b.setContent(rs.getString("content"));
            b.setWriter(rs.getString("writer"));
            b.setFilename(rs.getString("filename"));
            b.setOriginalFilename(rs.getString("original_filename"));
            b.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return b;
        });
    }

    /**
     * 게시글 단건 조회
     */
    public BoardDTO findById(Long id) {

        String sql = "SELECT * FROM board2 WHERE id = ?";

        return jdbc.queryForObject(sql, (rs, rn) -> {
            BoardDTO b = new BoardDTO();
            b.setId(rs.getLong("id"));
            b.setTitle(rs.getString("title"));
            b.setContent(rs.getString("content"));
            b.setWriter(rs.getString("writer"));
            b.setFilename(rs.getString("filename"));
            b.setOriginalFilename(rs.getString("original_filename"));
            b.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return b;
        }, id);
    }

    /**
     * 게시글 수정
     */
    public void update(BoardDTO board) {
        String sql = "UPDATE board2 SET title = ?, content = ? WHERE id = ?";
        jdbc.update(sql,
                board.getTitle(),
                board.getContent(),
                board.getId());
    }

    /**
     * 게시글 삭제
     */
    public void delete(Long id) {
        jdbc.update("DELETE FROM board2 WHERE id = ?", id);
    }
}