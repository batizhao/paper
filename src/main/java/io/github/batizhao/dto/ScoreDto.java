package io.github.batizhao.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public class ScoreDto {

    private String name;
    private Long total;

    public ScoreDto(String name, Long total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
