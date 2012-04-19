package li2latex.samples

import xml.NodeSeq

object SampleFields {
  val positionsFields: NodeSeq =
    <positions total="2">
      <position>
        <id>189394455</id>
        <title>Software Development Engineer</title>
        <summary>Dev of SharePoint Online</summary>
        <start-date>
          <year>2011</year>
          <month>6</month>
        </start-date>
        <is-current>true</is-current>
        <company>
          <id>1035</id>
          <name>Microsoft</name>
          <type>Public Company</type>
          <industry>Computer Software</industry>
          <ticker>MSFT</ticker>
        </company>
      </position>
      <position>
        <id>109528084</id>
        <title>Research Assistant</title>
        <start-date>
          <year>2009</year>
          <month>6</month>
        </start-date>
        <end-date>
          <year>2011</year>
          <month>6</month>
        </end-date>
        <is-current>false</is-current>
        <company>
          <id>4734</id>
          <name>Artificial Intelligence Lab, The University of Arizona</name>
          <size>10,001+ employees</size>
          <type>Educational Institution</type>
          <industry>Higher Education</industry>
        </company>
      </position>
    </positions>

  val publicationsFields: NodeSeq =
    <publications total="1">
      <publication>
        <id>1</id>
        <title>An Integrated Framework for Avatar Data Collection from the Virtual World: An Case Study in Second Life</title>
        <date>
          <year>2010</year>
          <month>12</month>
        </date>
      </publication>
    </publications>

  val patentsFields: NodeSeq =
    <patents total="1">
      <patent>
        <id>35</id>
        <title>This is for test purpose</title>
        <date>
          <year>2010</year>
          <month>3</month>
          <day>5</day>
        </date>
      </patent>
    </patents>

  val languagesFields: NodeSeq =
    <languages total="2">
      <language>
        <id>21</id>
        <language>
          <name>English</name>
        </language>
      </language>
      <language>
        <id>22</id>
        <language>
          <name>Chinese</name>
        </language>
      </language>
    </languages>

  val skillsFields: NodeSeq =
    <skills total="2">
      <skill>
        <id>6</id>
        <skill>
          <name>Java</name>
        </skill>
      </skill>
      <skill>
        <id>7</id>
        <skill>
          <name>C#</name>
        </skill>
      </skill>
    </skills>

  //  val certificationsFields: NodeSeq =

  val educationsFields: NodeSeq =
    <educations total="2">
      <education>
        <id>62008904</id>
        <school-name>University of Arizona, Eller College of Management</school-name>
        <activities>Received Research Fellowship from Science Foundation Arizona, Sep 2009</activities>
        <degree>M.S.</degree>
        <field-of-study>Management Information Systems</field-of-study>
        <start-date>
          <year>2009</year>
        </start-date>
        <end-date>
          <year>2011</year>
        </end-date>
      </education>
      <education>
        <id>62008886</id>
        <school-name>Shanghai Jiao Tong University</school-name>
        <notes></notes>
        <activities></activities>
        <degree>B.S.</degree>
        <field-of-study>Electrical Engineering</field-of-study>
        <start-date>
          <year>2004</year>
        </start-date>
        <end-date>
          <year>2008</year>
        </end-date>
      </education>
    </educations>

  val coursesFields: NodeSeq =
    <courses total="3">
      <course>
        <id>30</id>
        <name>Design and Analysis of Algorithms</name>
        <number>CSC 545</number>
      </course>
      <course>
        <id>31</id>
        <name>Principle of Compilation</name>
        <number>CSC 553</number>
      </course>
      <course>
        <id>32</id>
        <name>Parallel and Distributed Computing</name>
        <number>CSC 522</number>
      </course>
    </courses>

  val volunteerExperienceFields: NodeSeq =
    <volunteer>
      <volunteer-experiences total="1">
        <volunteer-experience>
          <id>33</id>
          <role>Volunteer</role>
          <organization>
            <id>19652</id>
            <name>United Way of King County</name>
          </organization>
        </volunteer-experience>
      </volunteer-experiences>
    </volunteer>

  val projectsFields: NodeSeq =
    <projects total="2">
      <project>
        <id>25</id>
        <name>Dark Web Forum Portal</name>
        <description>* Created distributed crawlers that run on Hadoop to collect all the messages in 29 online forums incrementally.
          * Built a web application using Struts 2 and Spring that provides search and browsing functionalities of all the information in the forums. The search engine is based on Lucene. EHCache is used to boost system performance.</description>
        <url>http://cri-portal.dyndns.org/</url>
      </project>
      <project>
        <id>27</id>
        <name>Real-world and virtual-world mapping</name>
        <description>* Created a .Net framework for collecting real-time avatar traces from Second Life.
          * Conducted sentiment analysis and social network analysis to study the characteristics of virtual worlds, compared to real world and other online communities.</description>
      </project>
    </projects>

  val summaryField: NodeSeq =
    <summary>SDE@Microsoft</summary>

  val contactInfoFields: NodeSeq =
    <phone-numbers total="1">
      <phone-number>
        <phone-type>mobile</phone-type>
        <phone-number>206-949-2950</phone-number>
      </phone-number>
    </phone-numbers>
    <im-accounts total="1">
      <im-account>
        <im-account-type>gtalk</im-account-type>
        <im-account-name>ximing.yu@gmail.com</im-account-name>
      </im-account>
    </im-accounts>
    <main-address>3505 167th Ct NE Apt EE102, Redmond WA 98052</main-address>
}
