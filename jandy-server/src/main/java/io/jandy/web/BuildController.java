package io.jandy.web;

import io.jandy.domain.Build;
import io.jandy.domain.BuildRepository;
import io.jandy.domain.ProfTreeNode;
import io.jandy.domain.ProjectRepository;
import io.jandy.exception.ResourceNotFoundException;
import io.jandy.service.GitHubService;
import org.eclipse.egit.github.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author JCooky
 * @since 2015-07-10
 */
@Controller
@RequestMapping("/builds")
public class BuildController {
  @Autowired
  private BuildRepository buildRepository;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private GitHubService github;

  @RequestMapping("/{id:\\d+}")
  public ModelAndView build(@PathVariable long id) throws IOException {
    Build build = buildRepository.findOne(id);
//    build.getCommit().getCommitterName()

    User user = null;
    if (build.getCommit() != null)
      user = github.getUser(build.getCommit().getCommitterName());

    return new ModelAndView("benchmark")
        .addObject("projects", projectRepository.findAll())
        .addObject("build", build)
        .addObject("prof", build.getProfContextDump())
        .addObject("committerAvatarUrl", user == null ? null : user.getAvatarUrl());
  }
}
