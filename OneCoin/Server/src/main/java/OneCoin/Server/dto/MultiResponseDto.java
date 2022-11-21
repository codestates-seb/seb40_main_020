package OneCoin.Server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MultiResponseDto<T> {
    private List<T> data;
}
