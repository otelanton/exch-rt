package com.account.accountservice.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteEvent implements Event{
  long id;
}
