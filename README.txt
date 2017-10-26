README.txt

Group Members <Name>:
	Echo Lisa Sky Aosen

============= OUTLINE ==============
#   Structure of the README.txt    #
====================================

	(1) Directory Hierarchy
	(2) General Program Execution Method
	(3) General Fuzzer Execution Method
	(4) Detail Program Execution Method
	(5) Detail Fuzzer Execution Method

	-> When you first download the project, there are pre-compiled classes in classes directort.
	-> All command execute under source directory.
	-> For quick check the function of project just try the Examples in section (2) and (3).
	-> For detailed execution methods, check instruction in section (4) and (5).

==== (1) Directory Hierarchy ====
#    Directory Hierarchy        #
=================================

	- source
		- <Name> * 4
			- source_code
				- *.java : Security Program JAVA Files
				- testcase
					- *.s : Mutation Based Fuzz Files
			- customized_fuzzer
				- *.java : Customed Fuzzer
		- classes
			- classes_<name>
				- *.class : Security Program class Files compiled from the correspongding - java files (generated through ant).
		- infile.txt : Echo's valid input file.
		- inkey.txt : Echo's valid input file.
		- README.txt
		- build.xml

				
========== (2) General Program Execution Method ============
#   This part introduces the general execution methods     #
#   of four security programs. 							   #
#   Since the execution command varies with programs, 	   #
#   details are demonstrated in the last section:		   #
#   Detail Program Execution Method.                       #
============================================================

	Command:

		$ ant clean
		$ ant run_program
		$ java -classpath <class_path> <class_name> <input_file> <bug0> <bug1> ... <bugn>

	Note:

		Different programs have various command pattern, details are in the last section DETAILS.

		<class_path>  - the directory where the corresponding *.class file saved.
		<class_name> - the class which main function written in.
		<input_file> - the input fuzz file's path.
		<bug0>...<bugn> - the corresponding boolean value of bugs, the number of bugs vary with programs.

	Example:

		$ ant clean
		$ ant run_program
		$ java -classpath classes/classes_echo SimpleDriver ./Echo/source_code/testcase/testcase_sky/1/fuzz.s false false false true
		 
		This command will run Sky's first group of test cases on Echo's program.

=========== (3) General Fuzzer Execution Method ============
#   This part introduces the general execution methods of  #
#   several customized fuzzers. 						   #
#   Details refer to: Detail Fuzzer Execution Method.      #
============================================================
	
	Command:

		$ ant clean
		$ ant run_fuzzer
		$ java -classpath <class_path> <class_name>
	
	Note:

		<class_path> - the directory where the corresponding *.class file saved. Following the pattern: classes/class_<Name> (with first character upercase)
		<class_name> - the class which main function written in: refer to the class in "customed_fuzzer" directory.

	Example:

		$ ant clean
		$ ant run_fuzzer
		$ java -classpath classes/classes_echo Fuzzer
		

============ (4) Detail Program Execution Method ===========
#   Details execution methods for four security programs   #
============================================================


----- Echo Program Execution Method -----

	Command:
		$ java -classpath classes/classes_echo SimpleDriver ./Echo/source_code/testcase/testcase_<name>/<num>/fuzz.s <bug0> <bug1> <bug2> <bug3>

	Note:
		<name> - replaced by one of other three lowercase group member names or "zzuf".
		<num>  - replaced by a number chosen from 1, 2, 3, 4.

	Example:
		$ java -classpath classes/classes_echo SimpleDriver ./Echo/source_code/testcase/testcase_sky/1/fuzz.s false false false true
		  This command will run Sky's first group of test cases on Echo's program.


----- Aosen Program Execution Method -----

	Command:
		$ java -classpath classes/classes_aosen CaesarCipher ./Aosen/source_code/testcase/fuzz_<name>_Aosen_1.s/fuzz_<name>_Aosen_<num>.s <bug0> <bug1> <bug2> <bug3> <bug4> <bug5> <bug6> 

	Note:
		<name> - replaced by one of other three group member names (with first character upercase) or "zzuf".
		<num>  - replaced by a number chosen from 1, 2, 3, 4.

	Example:
		$ java -classpath classes/classes_aosen CaesarCipher ./Aosen/source_code/testcase/fuzz_Echo_Aosen_1.s false false false false false false true


----- Lisa Program Execution Method -----

	Command:
		$ java -classpath classes/classes_lisa Cipher ./Lisa/source_code/testcase/fuzz_<name>_Lisa_<num>.s <bug0> <bug1> <bug2>

	Note:
		<name> - replaced by one of other three group member names (with first character upercase) or "zzuf".
		<num>  - replaced by a number chosen from 1, 2, 3, 4.

	Example:
		$ java -classpath classes/classes_lisa Cipher ./Lisa/source_code/testcase/fuzz_Echo_Lisa_1.s false false true



----- Sky Program Execution Method -----

	Command:
		$ java -classpath classes/classes_sky Base64Decoder ./Sky/source_code/testcase/fuzz_<name>_Sky_<num>.s <bug0> <bug1> <bug2>

	Note:
		<name> - replaced by one of other three group member names (with first character upercase) or "zzuf".
		<num>  - replaced by a number chosen from 1, 2, 3, 4.

	Example:
		$ java -classpath classes/classes_sky Base64Decoder ./Sky/source_code/testcase/fuzz_Echo_Sky_1.s false false true


=========== (5) Detail Fuzzer Execution Method ==========
#   Details fuzzer methods for four security programs   #
=========================================================

	Echo's Fuzzer:
		$ java -classpath classes/classes_echo Fuzzer

	Lisa's Fuzzer:
		$ java -classpath classes/classes_lisa Fuzzer

	Sky's Fuzzer:
		$ java -classpath classes/classes_SKY Fuzzer true true true

	Aosen's Fuzzer:
		$ java -classpath classes/classes_echo FuzzC
		$ java -classpath classes/classes_echo FuzzH
		$ java -classpath classes/classes_echo FuzzQ



