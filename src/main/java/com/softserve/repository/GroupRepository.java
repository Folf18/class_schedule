package com.softserve.repository;

import com.softserve.entity.Group;
import java.util.List;

public interface GroupRepository extends BasicRepository<Group, Long>{
    Long countGroupsWithTitleAndIgnoreWithId(Long id, String title);
    Long countGroupsWithTitle(String title);
    Long countByGroupId(Long id);
    List<Group> getDisabled();

}
