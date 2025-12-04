package kjhtest.test.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDTO {
    private int id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;

    private String filename;          // 서버에 저장된 파일명
    private String originalFilename;  // 사용자가 업로드한 원본 파일명
}
