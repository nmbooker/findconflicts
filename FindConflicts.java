import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import static java.nio.file.FileVisitResult.*;
import static java.nio.file.FileVisitOption.*;
import java.util.*;

public class FindConflicts
{
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("USAGE: java FindConflicts PATH");
            System.err.println("ERROR: Missing argument PATH");
            System.exit(1);
        }
        else
        {
            Path startingDir = Paths.get("/home/nick/passwords");
            Finder finder = new Finder("*.sync-conflict-*-*-*");
            try
            {
                Files.walkFileTree(startingDir, finder);
                System.exit(0);
            }
            catch (IOException e)
            {
                System.err.println("ERROR: " + e);
                System.exit(3);
            }
        }
    }

    public static class Finder
        extends SimpleFileVisitor<Path>
    {
        private final PathMatcher matcher;
        
        Finder(String pattern)
        {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }

        void found(Path file)
        {
            System.out.println(file);
        }

        @Override
        public FileVisitResult visitFile(
            Path file, BasicFileAttributes attrs)
        {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name))
            {
                found(file);
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(
            Path dir, BasicFileAttributes attrs)
        {
            if (dir.getFileName().equals(Paths.get("old-conflicts")))
            {
                return SKIP_SUBTREE;
            }
            else
            {
                return CONTINUE;
            }
        }
    }
}
