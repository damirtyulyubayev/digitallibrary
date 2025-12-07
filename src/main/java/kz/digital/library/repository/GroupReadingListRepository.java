package kz.digital.library.repository;

import kz.digital.library.domain.GroupReadingListItem;
import kz.digital.library.domain.ReadingListType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupReadingListRepository extends JpaRepository<GroupReadingListItem, Long> {
    List<GroupReadingListItem> findByGroupId(Long groupId);

    List<GroupReadingListItem> findByGroupIdAndType(Long groupId, ReadingListType type);
}
