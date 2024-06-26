package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.TestsResultDTO;
import com.example.lastone.model.entity.TestsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestsMapper {
    TestsEntity toTestsEntity(TestsResultDTO testsResultDTO);

    TestsResultDTO toTestsResultDto(TestsEntity tests);
}
