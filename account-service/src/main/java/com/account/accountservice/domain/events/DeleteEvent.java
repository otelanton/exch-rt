package com.account.accountservice.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DeleteEvent implements Event{
  long id;
}
