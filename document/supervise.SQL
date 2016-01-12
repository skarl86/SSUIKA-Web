load data infile 'C:\\parsed_Atom2.txt' into table atom_default ( atom_name, atom_type );

load data infile 'C:\\parsed_Atom2.txt' into table atom_default ( atom_name);

load data infile 'C:\\parsed_Atom2.txt' into table atom_default ( atom_name,conceptid, typeid);


delete from rule_ant where rule_id > 2;
delete from rule_con where rule_id > 2;
delete from rule_user where rule_id > 2;
delete from atom_value where atom_id >
delete from atom_value where value_id = -1
delete from atom_default where atom_id > 489836
DELETE FROM patient_opinion_rule WHERE id = ? AND opinion_id = ? AND rule_id = ?
//
atom_value ->

//rule 초기화
TRUNCATE TABLE `rule_ant`;
TRUNCATE TABLE `rule_con`;
TRUNCATE TABLE `patient_opinion_rule`;
TRUNCATE TABLE `rule_user`;
TRUNCATE TABLE `atom_default`;

TRUNCATE TABLE `valued_atom`;

http://183.101.198.97:8081/ssuika/setRule.jsp?patientID=56422&opinionID=1&antecedents=ALP_&consequents=LiverDisease_&authorID=1

http://183.101.198.97/