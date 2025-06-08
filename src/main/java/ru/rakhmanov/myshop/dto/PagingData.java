package ru.rakhmanov.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingData {
    private int pageNumber;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
