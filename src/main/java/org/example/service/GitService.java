package org.example.service;

import static org.example.util.Constants.maintainedResourceFormats;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.example.util.string.StringUtil;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GitService {

  public Map<String, InputStream> parseGitFiles(String gitURL) throws GitAPIException, IOException {
    Git git = Git.cloneRepository()
        .setURI(gitURL)
        .call();

    Repository repository = git.getRepository();

    Map<String, InputStream> files = new HashMap<>();

    // find the HEAD
    ObjectId lastCommitId = repository.resolve(Constants.HEAD);

    // a RevWalk allows to walk over commits based on some filtering that is defined
    try (RevWalk revWalk = new RevWalk(repository)) {
      RevCommit commit = revWalk.parseCommit(lastCommitId);
      // and using commit's tree find the path
      RevTree tree = commit.getTree();

      // now try to find a specific file
      try (TreeWalk treeWalk = new TreeWalk(repository)) {
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        while (treeWalk.next()) {

          Optional<String> fileExtension = StringUtil.getExtensionByStringHandling(treeWalk.getNameString());

          if (fileExtension.isPresent() && maintainedResourceFormats.stream().anyMatch(fileExtension.get()::equals)) {
            ObjectId objectId = treeWalk.getObjectId(0);
            ObjectLoader loader = repository.open(objectId);

            byte[] bytes = loader.getBytes();

            InputStream targetStream = new ByteArrayInputStream(bytes);
            files.put(treeWalk.getNameString(), targetStream);
          }
        }
      }
      revWalk.dispose();
    }
    //git.rm().call();
    return files;
  }

}
