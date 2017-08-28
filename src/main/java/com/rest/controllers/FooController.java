package com.rest.controllers;

import com.rest.helpers.RestPreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/foos")
class FooController {
  @Autowired
  private IFooSevice iFooSevice;

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public List<Foo> findAll() {
    return iFooSevice.findAll();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  public Foo findOne(@PathVariable("id") Long id) {
    return RestPreconditions.checkFound(service.findOne(id));
  }

  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Long create(@RequestBody Foo resource) {
    Preconditions.checkNotNull(resource);
    return iFooSevice.create(resource);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void update(@PathVariable("id") Long id, @RequestBody Foo resource) {
    Preconditions.checkNotNull(resource);
    RestPreconditions.checkNotNull(iFooSevice.getById(resource.getId()));
    iFooSevice.update(resource);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void delete(@PathVariable("id") Long id) {
    iFooSevice.deleteById(id);
  }
}
