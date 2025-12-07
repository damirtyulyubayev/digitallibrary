package kz.digital.library.web;

import kz.digital.library.domain.ReadingListItem;
import kz.digital.library.domain.ReadingListType;
import kz.digital.library.service.ReadingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reading-list")
@RequiredArgsConstructor
public class ReadingListController {

    private final ReadingListService readingListService;

    @PostMapping("/{userId}/add")
    public ReadingListItem addToList(@PathVariable Long userId,
                                     @RequestParam("bookId") Long bookId,
                                     @RequestParam("type") ReadingListType type) {
        return readingListService.addToReadingList(userId, bookId, type);
    }

    @GetMapping("/{userId}")
    public List<ReadingListItem> getList(@PathVariable Long userId,
                                         @RequestParam(value = "type", required = false) ReadingListType type) {
        return readingListService.getReadingList(userId, type);
    }
}
