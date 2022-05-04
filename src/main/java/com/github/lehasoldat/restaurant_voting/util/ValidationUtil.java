package com.github.lehasoldat.restaurant_voting.util;

import com.github.lehasoldat.restaurant_voting.error.AppException;
import com.github.lehasoldat.restaurant_voting.model.BaseEntity;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(BaseEntity entity) {
        if (entity.getId() != null) {
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, entity.getClass().getSimpleName() + " must be new");
        }
    }

    public static void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.getId() == null) {
            entity.setId(id);
        } else if (entity.getId()!=id) {
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, entity.getClass().getSimpleName() + " must have id = " + id);
        }
    }

}
