package kjhtest.test.domain;

import lombok.Data;

@Data
public class BoardFile {
    private Long id;
    private Long boardId;
    private String originalName;
    private String savedName;
    private String filePath;
    private long fileSize;
}

/*
| 필드명            | 설명                    |
| -------------- | --------------------- |
| `id`           | 파일의 고유번호(PK)          |
| `boardId`      | 어떤 게시글에 속한 파일인지       |
| `originalName` | 사용자가 올린 파일명           |
| `savedName`    | 서버에 저장된 파일명(UUID+파일명) |
| `filePath`     | 하드에 실제 저장된 경로         |

 */