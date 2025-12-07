package kz.digital.library.web;

import kz.digital.library.domain.GroupReadingListItem;
import kz.digital.library.domain.ReadingListType;
import kz.digital.library.domain.StudyGroup;
import kz.digital.library.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public StudyGroup create(@RequestBody Map<String, String> body) {
        return groupService.createGroup(body.get("name"), body.get("institution"), Long.valueOf(body.get("teacherId")));
    }

    @GetMapping("/teacher/{teacherId}")
    public List<StudyGroup> groups(@PathVariable Long teacherId) {
        return groupService.groupsForTeacher(teacherId);
    }

    @PostMapping("/{groupId}/assign")
    public GroupReadingListItem assign(@PathVariable Long groupId,
                                       @RequestParam Long bookId,
                                       @RequestParam ReadingListType type) {
        return groupService.assignBook(groupId, bookId, type);
    }

    @GetMapping("/{groupId}/list")
    public List<GroupReadingListItem> list(@PathVariable Long groupId,
                                           @RequestParam(required = false) ReadingListType type) {
        return groupService.groupReadingList(groupId, type);
    }
}
