package kjhtest.test.repository;

import kjhtest.test.domain.BoardFile;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardFileRepository {

    private final JdbcTemplate jdbc;

    // 파일 INSERT
    public void save(BoardFile file) {
        String sql = "INSERT INTO board_file (board_id, original_name, saved_name, file_path) VALUES (?,?,?,?)";
        jdbc.update(sql,
                file.getBoardId(),
                file.getOriginalName(),
                file.getSavedName(),
                file.getFilePath()
        );
    }

    // 게시글에 올라온 파일 리스트
    public List<BoardFile> findByBoardId(int boardId) {
        String sql = "SELECT * FROM board_file WHERE board_id=?";
        return jdbc.query(sql, (rs, rowNum) -> {
            BoardFile f = new BoardFile();
            f.setId(rs.getInt("id"));
            f.setBoardId(rs.getInt("board_id"));
            f.setOriginalName(rs.getString("original_name"));
            f.setSavedName(rs.getString("saved_name"));
            f.setFilePath(rs.getString("file_path"));
            return f;
        }, boardId);
    }

    // 파일 1개 조회 (다운로드용)
    public BoardFile findById(int id) {
        String sql = "SELECT * FROM board_file WHERE id=?";
        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            BoardFile f = new BoardFile();
            f.setId(rs.getInt("id"));
            f.setBoardId(rs.getInt("board_id"));
            f.setOriginalName(rs.getString("original_name"));
            f.setSavedName(rs.getString("saved_name"));
            f.setFilePath(rs.getString("file_path"));
            return f;
        }, id);
    }

    public void delete(BoardFile file) {
    }
}