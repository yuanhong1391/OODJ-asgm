# Generates initial NetBeans-compatible forms. Once edited in NetBeans Design view,
# maintain the .java/.form pair there; do not rerun this generator over your changes.
$ErrorActionPreference = 'Stop'
$destination = Join-Path $PSScriptRoot '../src/hms/ui/forms'
New-Item -ItemType Directory -Force $destination | Out-Null
$forms = [ordered]@{
    LoginForm = @('username|Username|text','password|Password|password')
    SetupForm = @('name|Your full name|text','username|Admin username|text','password|Password (at least 8 characters)|password','confirmPassword|Confirm password|password')
    UserForm = @('name|Full name *|text','username|Username *|text','role|Role *|ADMIN,MANAGER,DOCTOR,PATIENT','password|Password (blank keeps existing)|password','email|Email|text','phone|Phone|text','active|Active *|true,false')
    AssignmentForm = @('doctorId|Doctor *|combo','managerId|Medical manager *|combo')
    AssetForm = @('name|Asset name *|text','type|Asset type *|CONSULTATION_ROOM,WARD,BED,LAB,XRAY,IMAGING','parentId|Parent ward (beds only)|combo','status|Availability *|AVAILABLE,MAINTENANCE,INACTIVE')
    AllocationForm = @('assetId|Facility or bed *|combo','patientId|Patient *|combo','doctorId|Doctor (required for consultations/tests)|combo','requestId|Lab or imaging request|combo','start|Start (YYYY-MM-DDTHH:MM) *|text','end|End (YYYY-MM-DDTHH:MM) *|text','status|Allocation status *|BOOKED,COMPLETED,CANCELLED')
    RequestForm = @('doctorId|Requesting doctor *|combo','patientId|Patient *|combo','type|Test type *|LAB,XRAY,IMAGING','details|Doctor request details *|text','status|Request status *|OPEN,COMPLETED,CANCELLED')
    RateForm = @('consultationType|Consultation type *|text','amount|Base rate (MYR) *|text','active|Active *|true,false')
    InsuranceForm = @('name|Insurance network name *|text','active|Accepted *|true,false')
}
foreach ($formName in $forms.Keys) {
    $init = [System.Collections.Generic.List[string]]::new()
    $decl = [System.Collections.Generic.List[string]]::new()
    $mapping = [System.Collections.Generic.List[string]]::new()
    $components = [System.Collections.Generic.List[string]]::new()
    $init.Add('        setLayout(new java.awt.GridLayout(0, 2, 14, 12));')
    foreach ($definition in $forms[$formName]) {
        $parts = $definition.Split('|')
        $key = $parts[0]; $label = $parts[1]; $kind = $parts[2]
        $labelXml = [System.Security.SecurityElement]::Escape($label)
        $class = if ($kind -eq 'text') { 'JTextField' } elseif ($kind -eq 'password') { 'JPasswordField' } else { 'JComboBox<String>' }
        $xmlClass = if ($class.StartsWith('JComboBox')) { 'JComboBox' } else { $class }
        $init.Add("        ${key}Label = new javax.swing.JLabel(`"$label`");")
        $init.Add("        ${key}Field = new javax.swing.$class();")
        $comboProperty = ''
        if ($xmlClass -eq 'JComboBox') {
            $choices = if ($kind -eq 'combo') { @('') } else { $kind.Split(',') }
            $javaChoices = ($choices | ForEach-Object { '"' + $_ + '"' }) -join ', '
            $init.Add("        ${key}Field.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { $javaChoices }));")
            $items = ($choices | ForEach-Object { '<StringItem index="' + [array]::IndexOf($choices,$_) + '" value="' + $_ + '"/>' }) -join ''
            $comboProperty = '<Properties><Property name="model" type="javax.swing.ComboBoxModel" editor="org.netbeans.modules.form.editors2.ComboBoxModelEditor"><StringArray count="' + $choices.Count + '">' + $items + '</StringArray></Property></Properties><AuxValues><AuxValue name="JavaCodeGenerator_TypeParameters" type="java.lang.String" value="&lt;String&gt;"/></AuxValues>'
        }
        $init.Add("        ${key}Label.setLabelFor(${key}Field);")
        $init.Add("        add(${key}Label);")
        $init.Add("        add(${key}Field);")
        $decl.Add("    private javax.swing.JLabel ${key}Label;")
        $decl.Add("    private javax.swing.$class ${key}Field;")
        $mapping.Add("        result.put(`"$key`", ${key}Field);")
        $components.Add("    <Component class=`"javax.swing.JLabel`" name=`"${key}Label`"><Properties><Property name=`"text`" type=`"java.lang.String`" value=`"$labelXml`"/><Property name=`"labelFor`" type=`"java.awt.Component`" editor=`"org.netbeans.modules.form.ComponentChooserEditor`"><ComponentRef name=`"${key}Field`"/></Property></Properties></Component>")
        $components.Add("    <Component class=`"javax.swing.$xmlClass`" name=`"${key}Field`">$comboProperty</Component>")
    }
    $source = @"
package hms.ui.forms;

import hms.ui.EditorPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Edit component layout in NetBeans Design view. */
public class $formName extends EditorPanel {
    public $formName() {
        initComponents();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    @Override protected Map<String, JComponent> fields() {
        Map<String, JComponent> result = new LinkedHashMap<>();
$($mapping -join "`n")
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
$($init -join "`n")
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
$($decl -join "`n")
    // End of variables declaration//GEN-END:variables
}
"@
    $formXml = @"
<?xml version="1.0" encoding="UTF-8" ?>
<Form version="1.3" maxVersion="1.9" type="org.netbeans.modules.form.forminfo.JPanelFormInfo">
  <AuxValues>
    <AuxValue name="FormSettings_autoResourcing" type="java.lang.Integer" value="0"/>
    <AuxValue name="FormSettings_autoSetComponentName" type="java.lang.Boolean" value="false"/>
    <AuxValue name="FormSettings_generateFQN" type="java.lang.Boolean" value="true"/>
    <AuxValue name="FormSettings_generateMnemonicsCode" type="java.lang.Boolean" value="false"/>
    <AuxValue name="FormSettings_listenerGenerationStyle" type="java.lang.Integer" value="0"/>
    <AuxValue name="FormSettings_variablesLocal" type="java.lang.Boolean" value="false"/>
    <AuxValue name="FormSettings_variablesModifier" type="java.lang.Integer" value="2"/>
  </AuxValues>
  <Layout class="org.netbeans.modules.form.compat2.layouts.DesignGridLayout">
    <Property name="rows" type="int" value="0"/>
    <Property name="columns" type="int" value="2"/>
    <Property name="horizontalGap" type="int" value="14"/>
    <Property name="verticalGap" type="int" value="12"/>
  </Layout>
  <SubComponents>
$($components -join "`n")
  </SubComponents>
</Form>
"@
    [System.IO.File]::WriteAllText((Join-Path $destination "$formName.java"), $source)
    [System.IO.File]::WriteAllText((Join-Path $destination "$formName.form"), $formXml)
}
Write-Output "Created $($forms.Count) NetBeans editor forms."
