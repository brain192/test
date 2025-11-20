package kjhtest.test.repository;

import kjhtest.test.domain.BoardDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardRepository {
    /**
     * BoardRepository 클래스
     * ----------------------------------------
     * - JDBC 기반으로 DB에 직접 접근하는 계층(DAO, Data Access Object)
     * - 역할: SQL 실행 및 데이터 조회/저장 등의 작업 수행
     * - 서비스(Service) → 저장소(Repository) → DB 순으로 계층 구조로 동작
     * - @Repository 애노테이션: 스프링 컨테이너에 빈으로 등록되며, 데이터 관련 예외를 스프링의 DataAccessException으로 변환 처리
     */
    // JdbcTemplate 주입: 스프링이 제공하는 JDBC 편의 클래스, 데이터베이스 연결과 SQL 실행, 예외 변환 등을 지원
    private final JdbcTemplate jdbcTemplate;

    // 생성자: JdbcTemplate을 주입 받아 초기화
    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 전체 게시글 수를 조회하는 메서드
     * - SQL: board2 테이블의 전체 행(Row) 수를 계산하여 반환
     * - queryForObject: 단일 값을 조회할 때 사용, 여기서는 COUNT 결과를 Integer 타입으로 반환
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM board2";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 특정 페이지의 게시글 목록을 조회하는 메서드 (페이징 처리: LIMIT + OFFSET 사용)
     * @param offset 시작 위치(몇 번째 행부터), @param pageSize 한 페이지에 표시할 행 수
     * - SQL: id 내림차순으로 정렬된 게시글 중 지정된 개수만큼 조회
     * - ? 플레이스홀더로 파라미터 바인딩 처리 (SQL Injection 방지)
     * - query 메서드와 람다(RowMapper) 사용: ResultSet에서 BoardDTO 객체로 매핑
     */
    public List<BoardDTO> findPage(int offset, int pageSize) {
        String sql = "SELECT id, title, writer, created_at FROM board2 ORDER BY id DESC LIMIT ? OFFSET ?";
        // 쿼리 실행 및 결과 매핑: 각 행을 BoardDTO 객체로 변환
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BoardDTO dto = new BoardDTO();
            dto.setId(rs.getLong("id"));                           // 게시글 ID 컬럼 값
            dto.setTitle(rs.getString("title"));                   // 게시글 제목 컬럼 값
            dto.setWriter(rs.getString("writer"));                 // 작성자 컬럼 값
            // created_at은 TIMESTAMP 형태이므로 LocalDateTime으로 변환하여 저장
            dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return dto;
        }, pageSize, offset);
    }

    /**
     * 게시글을 저장하는 메서드 (신규 작성)
     * @param board 저장할 게시글 정보가 담긴 BoardDTO 객체
     * - SQL: INSERT 문으로 board2 테이블에 새 행 추가
     * - PreparedStatement 형태로 ? 플레이스홀더에 값 바인딩
     * - update 메서드 사용: INSERT/UPDATE/DELETE 문 실행
     */
    public void save(BoardDTO board) {
        String sql = "INSERT INTO board2 (title, content, writer, filename, original_filename) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                board.getTitle(),            // 첫 번째 물음표(?) -> title 값
                board.getContent(),          // 두 번째 물음표(?) -> content 값
                board.getWriter(),           // 세 번째 물음표(?) -> writer 값
                board.getFilename(),         // 네 번째 물음표(?) -> filename 값
                board.getOriginalFilename()  // 다섯 번째 물음표(?) -> original_filename 값
        );
    }

    /**
     * 모든 게시글을 조회하는 메서드
     * @return BoardDTO 객체 리스트 (모든 게시글 정보)
     * - SQL: board2 테이블의 모든 컬럼을 조회하여 id 내림차순으로 정렬
     * - query 메서드와 람다(RowMapper) 사용: 각 행을 BoardDTO 객체로 매핑
     */
    public List<BoardDTO> findAll() {
        String sql = "SELECT * FROM board2 ORDER BY id DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BoardDTO board = new BoardDTO();
            board.setId(rs.getLong("id"));                       // ID 컬럼 값
            board.setTitle(rs.getString("title"));               // 제목 컬럼 값
            board.setContent(rs.getString("content"));           // 내용 컬럼 값
            board.setWriter(rs.getString("writer"));             // 작성자 컬럼 값
            board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime()); // 작성 시간
            return board;
        });
    }

    /**
     * ID로 특정 게시글을 조회하는 메서드
     * @param id 조회할 게시글의 ID
     * @return ID에 해당하는 게시글 정보(BoardDTO)
     * - SQL: WHERE 절로 특정 ID의 행만 조회
     * - queryForObject 사용: 단일 행 조회 후 BoardDTO로 매핑
     * - 예외: 해당 ID가 없으면 EmptyResultDataAccessException 발생 가능
     */
    public BoardDTO findById(Long id) {
        String sql = "SELECT * FROM board2 WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BoardDTO board = new BoardDTO();
            board.setId(rs.getLong("id"));                           // ID 컬럼 값
            board.setTitle(rs.getString("title"));                   // 제목 컬럼 값
            board.setContent(rs.getString("content"));               // 내용 컬럼 값
            board.setWriter(rs.getString("writer"));                 // 작성자 컬럼 값
            board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime()); // 작성 시간
            // 파일 관련 컬럼 매핑
            board.setFilename(rs.getString("filename"));             // 저장된 파일 이름
            board.setOriginalFilename(rs.getString("original_filename")); // 원본 파일 이름
            return board;
        }, id);
    }

    /**
     * 게시글을 삭제하는 메서드
     * @param id 삭제할 게시글의 ID
     * - SQL: DELETE 문으로 특정 ID의 행 삭제
     */
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM board2 WHERE id = ?", id);
    }

    /**
     * 게시글을 수정하는 메서드
     * @param board 수정할 게시글 정보가 담긴 BoardDTO 객체 (ID, 새 제목, 새 내용 포함)
     * - SQL: UPDATE 문으로 특정 ID의 제목과 내용을 수정
     */
    public void update(BoardDTO board) {
        jdbcTemplate.update("UPDATE board2 SET title=?, content=? WHERE id=?",
                board.getTitle(),    // 첫 번째 물음표(?) -> 수정할 제목
                board.getContent(),  // 두 번째 물음표(?) -> 수정할 내용
                board.getId()        // 세 번째 물음표(?) -> 수정할 행의 ID
        );
    }
}
