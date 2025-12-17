package kjhtest.test.repository;

import kjhtest.test.domain.BoardFile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class BoardFileRepository {
    private final JdbcTemplate jdbc;

    public BoardFileRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public long save(BoardFile bf) {
        String sql = "INSERT INTO board_file (board_id, original_name, saved_name, file_path, file_size) VALUES (?, ?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, bf.getBoardId());
            ps.setString(2, bf.getOriginalName());
            ps.setString(3, bf.getSavedName());
            ps.setString(4, bf.getFilePath());
            //ps.setLong(5, bf.getFileSize() == null ? 0L : bf.getFileSize());
            ps.setLong(5, bf.getFileSize());
            return ps;
        }, kh);
        Number k = kh.getKey();
        return (k != null) ? k.longValue() : 0L;
    }

    public List<BoardFile> findByBoardId(long boardId) {
        String sql = "SELECT * FROM board_file WHERE board_id = ?";
        return jdbc.query(sql, (rs, rn) -> {
            BoardFile f = new BoardFile();
            f.setId(rs.getLong("id"));
            f.setBoardId(rs.getLong("board_id"));
            f.setOriginalName(rs.getString("original_name"));
            f.setSavedName(rs.getString("saved_name"));
            f.setFilePath(rs.getString("file_path"));
            f.setFileSize(rs.getLong("file_size"));
            return f;
        }, boardId);
    }

    public BoardFile findById(long id) {
        String sql = "SELECT * FROM board_file WHERE id = ?";
        return jdbc.queryForObject(sql, (rs, rn) -> {
            BoardFile f = new BoardFile();
            f.setId(rs.getLong("id"));
            f.setBoardId(rs.getLong("board_id"));
            f.setOriginalName(rs.getString("original_name"));
            f.setSavedName(rs.getString("saved_name"));
            f.setFilePath(rs.getString("file_path"));
            f.setFileSize(rs.getLong("file_size"));
            return f;
        }, id);
    }

    public void delete(long id) {
        jdbc.update("DELETE FROM board_file WHERE id = ?", id);
    }
}