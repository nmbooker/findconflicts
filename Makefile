all: FindConflicts.class

FindConflicts.class: FindConflicts.java
	javac FindConflicts.java

clean:
	$(RM) FindConflicts.class
.PHONY: all clean
