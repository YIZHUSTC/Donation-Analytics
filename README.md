# Donation Analytics

The program reads from input file line by line, processes each line respectively and immediately write out to an output file when necessary.

* Once become a repeat donor, always a repeat donor, no matter which year the donor contribute as long as the record appears after the record that make this donor a repeat donor.

#### To run the program:
chmod +x run.sh

./run.sh

#### To change the paths and names of input and output files, edit run.sh in the following format:
java -jar src/donation-analytics.jar *Input_file Percentile_file Output_file*

#### To run the program again with new data, please delete output file first before running.
