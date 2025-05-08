package com.trippia.trippia.mapper;

import com.trippia.trippia.dto.User.UserResponse;
import com.trippia.trippia.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
