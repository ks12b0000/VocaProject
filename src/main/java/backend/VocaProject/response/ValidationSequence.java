package backend.VocaProject.response;

import backend.VocaProject.response.ValidationGroup.NotBlankGroup;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, NotBlankGroup.class,  ValidationGroup.EmailGroup.class, ValidationGroup.PatternGroup.class, ValidationGroup.NotNullGroup.class})
public interface ValidationSequence {
}
