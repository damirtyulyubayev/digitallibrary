package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.domain.GroupReadingListItem;
import kz.digital.library.domain.ReadingListType;
import kz.digital.library.domain.StudyGroup;
import kz.digital.library.domain.LibraryUser;
import kz.digital.library.repository.BookRepository;
import kz.digital.library.repository.GroupReadingListRepository;
import kz.digital.library.repository.StudyGroupRepository;
import kz.digital.library.repository.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final StudyGroupRepository groupRepository;
    private final GroupReadingListRepository groupReadingListRepository;
    private final LibraryUserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public StudyGroup createGroup(String name, String institution, Long teacherId) {
        LibraryUser teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        StudyGroup group = StudyGroup.builder()
                .name(name)
                .institution(institution)
                .teacher(teacher)
                .build();
        return groupRepository.save(group);
    }

    public List<StudyGroup> groupsForTeacher(Long teacherId) {
        return groupRepository.findByTeacherId(teacherId);
    }

    @Transactional
    public GroupReadingListItem assignBook(Long groupId, Long bookId, ReadingListType type) {
        StudyGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        GroupReadingListItem item = GroupReadingListItem.builder()
                .group(group)
                .book(book)
                .type(type)
                .build();
        return groupReadingListRepository.save(item);
    }

    public List<GroupReadingListItem> groupReadingList(Long groupId, ReadingListType type) {
        if (type == null) {
            return groupReadingListRepository.findByGroupId(groupId);
        }
        return groupReadingListRepository.findByGroupIdAndType(groupId, type);
    }
}
