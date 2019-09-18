package org.javahub.submarine.modules.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javahub.submarine.common.base.BaseDto;
import org.javahub.submarine.modules.system.entity.DictionaryItem;
import org.javahub.submarine.modules.system.mapstruct.DictionaryItemMapStruct;
import org.mapstruct.factory.Mappers;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DictionaryItemDto extends BaseDto {


    /**
     * 字典id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long dictionaryId;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序
     */
    private Integer sort;


    public static DictionaryItem toEntity(DictionaryItemDto dictionaryItemDto) {
        DictionaryItemMapStruct mapStruct = Mappers.getMapper( DictionaryItemMapStruct.class );
        return mapStruct.toEntity(dictionaryItemDto);
    }

    public static DictionaryItemDto toDto(DictionaryItem dictionaryItem) {
        DictionaryItemMapStruct mapStruct = Mappers.getMapper( DictionaryItemMapStruct.class );
        return mapStruct.toDto(dictionaryItem);
    }

}